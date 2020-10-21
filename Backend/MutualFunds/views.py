from django.shortcuts import render
import pandas as pd
# Create your views here.
import os
import json
from django.http import HttpResponse
workpath = os.path.dirname(os.path.abspath(os.path.dirname(__package__)))

def index(request,amount,id):
    if id==1:
        file = 'Backend\\templates\\dataset\\' + "discover_tax_MF.csv"
    if id==2:
        file = 'Backend\\templates\\dataset\\' + "reugular_income_MF.csv"
    data = pd.read_csv(os.path.join(workpath, file))
    temp = list()
    for i in data['1(%)'].astype(float).tolist():
        if i == 0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount, i, 1))
    data['1Returns'] = temp
    temp = list()
    for i in data['3(%)'].astype(float).tolist():
        if i == 0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount, i, 1))
    data['3Returns'] = temp
    temp = list()
    for i in data['5(%)'].astype(float).tolist():
        if i == 0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount, i, 1))
    data['5Returns'] = temp
    temp = list()
    for i in data['10(%)'].astype(float).tolist():
        if i == 0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount, i, 1))
    data['10Returns'] = temp
    data1 = dict()
    data1["Rating"] = data["Rating"].astype(str).tolist()
    data1["Name"] = data["Fund Name"].astype(str).tolist()
    data1["1Returns"] = data["1Returns"].astype(str).tolist()
    data1["3Returns"] = data["3Returns"].astype(str).tolist()
    data1["5Returns"] = data["5Returns"].astype(str).tolist()
    data1["10Returns"] = data["10Returns"].astype(str).tolist()
    data1["1(%)"] = data["1(%)"].astype(str).tolist()
    data1["3(%)"] = data["3(%)"].astype(str).tolist()
    data1["5(%)"] = data["5(%)"].astype(str).tolist()
    data1["10(%)"] = data["10(%)"].astype(str).tolist()
    result = HttpResponse(json.dumps(data1, ensure_ascii=False), content_type="application/json")
    return result


def growth(request,amount,risk):
    file = 'Backend\\templates\\dataset\\' + 'grow_wealth_' + risk + "_risk_MF.csv"
    data = pd.read_csv(os.path.join(workpath,file))
    temp = list()
    for i in data['1(%)'].astype(float).tolist():
        if i==0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount,i,1))
    data['1Returns'] = temp
    temp = list()
    for i in data['3(%)'].astype(float).tolist():
        if i == 0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount, i, 1))
    data['3Returns'] = temp
    temp = list()
    for i in data['5(%)'].astype(float).tolist():
        if i == 0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount, i, 1))
    data['5Returns'] = temp
    temp = list()
    for i in data['10(%)'].astype(float).tolist():
        if i == 0.0:
            temp.append(0.0)
        else:
            temp.append(maturityCalculator(amount, i, 1))
    data['10Returns'] = temp
    data1 = dict()
    data1["Rating"] = data["Rating"].astype(str).tolist()
    data1["Name"] = data["Fund Name"].astype(str).tolist()
    data1["1Returns"] = data["1Returns"].astype(str).tolist()
    data1["3Returns"] = data["3Returns"].astype(str).tolist()
    data1["5Returns"] = data["5Returns"].astype(str).tolist()
    data1["10Returns"] = data["10Returns"].astype(str).tolist()
    data1["1(%)"] = data["1(%)"].astype(str).tolist()
    data1["3(%)"] = data["3(%)"].astype(str).tolist()
    data1["5(%)"] = data["5(%)"].astype(str).tolist()
    data1["10(%)"] = data["10(%)"].astype(str).tolist()
    result = HttpResponse(json.dumps(data1, ensure_ascii=False), content_type="application/json")
    return result

def maturityCalculator(amount, rate, tenure):
    i = rate / 1200
    n = 12 * tenure
    maturity = (amount * ((1 + i) ** n - 1) / i) * (1 + i)
    return round(maturity,0)