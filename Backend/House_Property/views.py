import csv

from django.http import HttpResponse
import json
import requests
from bs4 import BeautifulSoup
import warnings
from sklearn.linear_model import LinearRegression
import numpy as np
from sklearn.preprocessing import PolynomialFeatures
from datetime import datetime
warnings.filterwarnings("ignore")
import pandas as pd
import statsmodels.api as sm
import os
workpath = os.path.dirname(os.path.abspath(os.path.dirname(__package__)))
user = ["Mozilla/5.0","user-agent 2.0","user-agent 9.0", " user-agent 4.0" , "user-agent 5.0" , "Mozilla/7.0"]

def prediction(request,location,time_t,sqft,today):
    year = time_t.split("-")[0]
    cols = ["Inflation", "Dadar", "Chembur", "Churchgate"]
    cols.remove(location)
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\House.csv'))
    df = df.drop(columns=cols)
    df = df.dropna()
    df['Date'] = pd.to_datetime(df['Date'])
    df = df.set_index('Date')
    y = df[location].resample('MS').mean()
    mod = sm.tsa.statespace.SARIMAX(y, order=(1, 0, 0), seasonal_order=(0, 1, 1, 3), enforce_stationarity=False,
                                    enforce_invertibility=False)
    results = mod.fit()
    pred = results.get_prediction(start=pd.to_datetime('2020-01-01'), end=pd.to_datetime('2050-06-01'), dynamic=False)
    forcast = pd.to_datetime(time_t)
    forecast = pred.predicted_mean[forcast]


    ef = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\House.csv'))
    ef = ef.dropna()
    X = ef.Date
    Y = []
    for i in X:
        b_date = datetime.strptime(i, '%Y-%m-%d')
        temp = (datetime.today() - b_date).days / 365
        Y.append(round(temp, 1))
    ef['Years'] = Y
    date = datetime.strptime(time_t, '%Y-%m-%d')
    temp = (datetime.today() - date).days / 365
    investYear = round(temp, 1)
    X1 = ef.drop(columns=['Churchgate', 'Dadar', 'Chembur', 'Date','Years'])
    Y1 = ef[location]
    c = open(os.path.join(workpath, 'Backend\\templates\\dataset\\Inflation.csv'))
    reader = csv.reader(c)
    for row in reader:
        if row[0] == year:
            inf = row[1]
        if row[0] == "2020":
            inflation = row[1]
    polynomial_features = PolynomialFeatures(degree=2)
    x_poly = polynomial_features.fit_transform(X1)
    x_poly_test = polynomial_features.fit_transform(np.array([inf]).reshape(1, -1))
    model = LinearRegression()
    model.fit(x_poly, Y1)
    poly_pred = model.predict(x_poly_test)
    # future = pow(1+float(inflation),investYear)
    # future = today*future
    final = round(0.2 * forecast + 0.8 * poly_pred[0],4)
    data = dict()
    data['Answer'] = str(int(final*sqft))
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

def index(request,bhk,location,price):
    data = dict()
    headers = {"User-Agent": user[np.random.randint(0, 5)]}
    URL = "https://www.99acres.com/" + str(bhk)+"-bhk-property-in-"
    if location == "Dadar":
        URL = URL + "dadar-mumbai-south-ffid?"
    elif location == "Chembur":
        URL = URL + "chembur-mumbai-harbour-ffid?"
    elif location == "Churchgate":
        URL = URL + "churchgate-mumbai-south-ffid?"
    if price != 0 :
        URL = URL + "price_max=" + str(price)
    r = requests.get(URL)
    soup = BeautifulSoup(r.content, "html5lib")
    table = soup.find('div')
    flatname = list()
    prices = list()
    carpet = list()
    baths = list()
    name = list()
    description = list()
    count = list()
    j = 0
    countRow = 0
    for row in table.findAll('td', attrs={'class': "list_header_bold srpTuple__spacer10"}):
        name.append(row.text)
    for row in table.findAll('table', attrs={'class': "srpTuple__tupleTable"}):
        flatname.append(row.h2.text)
    for row in table.findAll('div', attrs={'class': "body_med", 'id': "srp_tuple_description"}):
        # print(row.h2)
        temp = row.text
        temp = temp.replace("\n", " ")
        description.append(temp)
        # description
    for row in table.findAll('td', attrs={'class': "srpTuple__midGrid title_semiBold srpTuple__spacer16"}):
        if j % 3 == 0:
            if price!=0:
                temp = row.text
                temp = temp.split(" ")
                x = temp[1]
                y = temp[2]
                if "." in x:
                    x = x.replace(".","")
                if "Lac" in y:
                    x = x + "0"*5
                elif "Cr" in y:
                    x = x + "0"*7
                if int(x) < price:
                    prices.append(row.text)
                    count.append(countRow)
            countRow = countRow + 1
        elif j % 3 == 1:
            carpet.append(row.text)
        elif j % 3 == 2:
            baths.append(row.text)
        j = j + 1
    final_flatname = list()
    final_carpet = list()
    final_baths = list()
    final_name = list()
    final_description = list()
    for x in count:
        final_baths.append(baths[x])
        final_carpet.append(carpet[x])
        final_description.append(description[x])
        final_flatname.append(flatname[x])
        final_name.append(name[x])
    if len(final_name) > 0:
        data['Areaname'] = final_flatname
        data['Flatname'] = final_name
        data['Price'] = prices
        data['Bathrooms'] = final_baths
        data['Carpet'] = final_carpet
        data['Description'] = final_description
    print(table)
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result
