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

def prediction(request,location,time,sqft):
    year = time.split("-")[0]
    cols = ["Inflation", "Dadar", "Chembur", "Churchgate"]
    cols.remove(location)
    df = pd.read_csv('templates/House.csv')
    df = df.drop(columns=cols)
    df = df.dropna()
    df['Date'] = pd.to_datetime(df['Date'])
    df = df.set_index('Date')
    y = df[location].resample('MS').mean()
    mod = sm.tsa.statespace.SARIMAX(y, order=(1, 0, 0), seasonal_order=(0, 1, 1, 3), enforce_stationarity=False,
                                    enforce_invertibility=False)
    results = mod.fit()
    pred = results.get_prediction(start=pd.to_datetime('2020-01-01'), end=pd.to_datetime('2050-06-01'), dynamic=False)
    forcast = pd.to_datetime(time)
    forecast = pred.predicted_mean[forcast]

    ef = pd.read_csv('templates/House.csv')
    ef = ef.dropna()
    X = ef.Date
    Y = []
    inflation = pd.read_csv('templates/Inflation.csv')
    for i in X:
        b_date = datetime.strptime(i, '%Y-%m-%d')
        temp = (datetime.today() - b_date).days / 365
        Y.append(round(temp, 1))
    ef['Years'] = Y
    X1 = ef.drop(columns=['Churchgate', 'Dadar', 'Chembur', 'Date'])
    Y1 = ef[location]
    inf = inflation[inflation['Year'] == year]['inflation forecast'].to_numpy()[0]
    polynomial_features = PolynomialFeatures(degree=3)
    x_poly = polynomial_features.fit_transform(X1)
    x_poly_test = polynomial_features.fit_transform(np.array([inf, 3.0]).reshape(1, -1))
    model = LinearRegression()
    model.fit(x_poly, Y1)
    poly_pred = model.predict(x_poly_test)

    final = round(0.4 * forecast + 0.6 * poly_pred[0],4)
    data = dict()
    data['Answer'] = str(final*sqft)
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

def index(request,bhk,location,price):
    data = dict()
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
    j = 0
    description = list()
    for row in table.findAll('td', attrs={'class': "list_header_bold srpTuple__spacer10"}):
        name.append(row.text)
    for row in table.findAll('table', attrs={'class': "srpTuple__tupleTable"}):
        flatname.append(row.h2.text)
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
            else:
                prices.append(row.text)
        elif j % 3 == 1:
            carpet.append(row.text)
        elif j % 3 == 2:
            baths.append(row.text)
        j = j + 1
        # pair of 3 for one house
    for row in table.findAll('div', attrs={'class': "body_med", 'id': "srp_tuple_description"}):
        # print(row.h2)
        temp = row.text
        temp = temp.replace("\n" , " ")
        description.append(temp)
        # description
    if len(prices) > 0:
        data['Areaname'] = flatname
        data['Flatname'] = name
        data['Price'] = prices
        data['Bathrooms'] = baths
        data['Carpet'] = carpet
        data['Description'] = description
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result
