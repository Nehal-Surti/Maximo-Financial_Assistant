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
user = ["Mozilla/5.0","useragent/2.0","useragent/9.0", " useragent/4.0" , "useragent/5.0" , "Mozilla/7.0"]

def prediction(request,weight,time_t,today):
    s = "Price"
    year = int(time_t.split("-")[0])
    d = time_t.split("-")[0] + "-" + time_t.split("-")[1] + "-" + "01"
    cols = ["Inflation","USD"]
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Gold.csv'))
    df = df.drop(columns=cols)
    df = df.dropna()
    df['Date'] = pd.to_datetime(df['Date'])
    df = df.set_index('Date')
    y = df[s].resample('MS').mean()
    mod = sm.tsa.statespace.SARIMAX(y,order=(1,0,0),enforce_stationarity=False,enforce_invertibility=False)
    results = mod.fit()
    pred = results.get_prediction(start=pd.to_datetime('2020-01-01'),end=pd.to_datetime(d), dynamic=False)
    print(pred.predicted_mean)
    forcast = pd.to_datetime(d)
    # forcast = datetime.strptime(time_t, '%Y-%m-%d')
    forecast = pred.predicted_mean[forcast]

    ef = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Gold.csv'))
    ef = ef.dropna()
    X = ef.Date
    Y = []
    inflation = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Inflation.csv'))
    usd = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\USD.csv'))
    for i in X:
      b_date = datetime.strptime(i, '%Y-%m-%d')
      temp = (datetime.today() - b_date).days/365
      Y.append(round(temp,1))
    # time_t = pd.to_datetime(time_t)
    date = datetime.strptime(time_t, '%Y-%m-%d')
    temp = (datetime.today() - date).days / 365
    investYear = round(temp, 1)
    ef['Years'] = Y
    X1 = ef.drop(columns=['Price','Date'])
    Y1 = ef[s]
    inf = inflation[inflation['Year'] == year]['inflation forecast'].to_numpy()[0]
    dollar = usd[usd['Date'] == time_t]['USD'].to_numpy()[0]
    polynomial_features= PolynomialFeatures(degree=2)
    x_poly = polynomial_features.fit_transform(X1)
    x_poly_test = polynomial_features.fit_transform(np.array([inf,dollar,investYear]).reshape(1,-1))
    model = LinearRegression()
    model.fit(x_poly, Y1)
    poly_pred = model.predict(x_poly_test)

    future = pow(1 + float(inf), investYear)
    future = today * future
    final = round(0.4 * forecast + 0.3 * poly_pred[0] + 0.3 * future, 4)
    data = dict()
    if weight<10:
        data['Answer'] = str(int(final * weight))
    else:
        data['Answer'] = str(int(final*(weight/10)))
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

def index(request):
    headers = {"User-Agent": user[np.random.randint(0, 5)]}
    URL = "https://www.google.com/search?sxsrf=ALeKk02LRi6GYrfd6lf09UuibKJJu-SuwA%3A1599479462354&ei=ph5WX9WQFbGW4-EPyNuNoA0&q=gold+price&oq=gold+price&gs_lcp=CgZwc3ktYWIQAzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQR1AAWABghO0KaABwAXgAgAEAiAEAkgEAmAEAqgEHZ3dzLXdpesABAQ&sclient=psy-ab&ved=0ahUKEwjV-7ap_dbrAhUxyzgGHchtA9QQ4dUDCA0&uact=5"
    # price_min , price_max, area_min, area_max
    r = requests.get(URL)
    soup = BeautifulSoup(r.content, "html5lib")
    table = soup.findAll('div', attrs={'class': "BNeawe deIvCb AP7Wnd"})
    temp = str(table[1]).split(":")[1].split(" ")[1].replace(",", "")
    data = dict()
    data['Answer'] = temp
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result


from django.shortcuts import render

# Create your views here.
