import json

import pandas as pd
import numpy as np
from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
from sklearn.linear_model import Lasso
from sklearn.model_selection import train_test_split
import os
workpath = os.path.dirname(os.path.abspath(os.path.dirname(__package__)))


def index(request,days,name):
    path_dict = {'icici': 'Backend\\templates\\content\\icici.csv', 'hdfc': 'Backend\\templates\\content\\hdfc.csv', 'bajaj': 'Backend\\templates\\content\\bajaj.csv',
                 'cipla': 'Backend\\templates\\content\\cipla.csv',
                 'gail': 'Backend\\templates\\content\\gail.csv', 'hul': 'Backend\\templates\\content\\hul.csv', 'itc': 'Backend\\templates\\content\\itc.csv',
                 'ongc': 'Backend\\templates\\content\\ongc.csv',
                 'tcs': 'Backend\\templates\\content\\tcs.csv', 'titan': 'Backend\\templates\\content\\titan.csv'}
    address = os.path.join(workpath,path_dict[name])
    df = pd.read_csv(address)
    data = dict()
    data["Today"] = df.tail(1)["Close"].astype(str).tolist()[0]
    forecast_out = days  # Number of how many days to forecast
    # df['Date'] = pd.to_datetime(df.Date, format='%Y-%m-%d')
    df['Date'] = pd.to_datetime(df.Date, format='%d-%m-%Y')  # Converts string to datetime
    df = df.set_index('Date')  # Set the index of dataframe to date column
    df['Prediction'] = df['Close'].shift(-forecast_out)
    x = np.array(df.drop(['Prediction'], 1))
    x = x[:-forecast_out]
    y = np.array(df['Prediction'])
    y = y[:-forecast_out]
    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2)

    lasso_model = Lasso()
    lasso_model.fit(x_train, y_train)

    x_forecast = np.array(df.drop(['Prediction'], 1))[-forecast_out:]
    lasso_model_forecast_prediction = lasso_model.predict(x_forecast)

    data["Future"] = str(round(lasso_model_forecast_prediction[-1],2))
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result