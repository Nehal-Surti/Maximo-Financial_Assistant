import json
import pandas as pd
from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
import os
workpath = os.path.dirname(os.path.abspath(os.path.dirname(__package__)))

def index(request,id):
    if id==1:
        data = TaxBonds()
    if id==2:
        data = GSec()
    if id==3:
        data = CorpBonds()
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result
def GSec():
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\GSec.csv'))
    data = dict()
    data["Symbol"] = df["SYMBOL"].astype(str).tolist()
    data["Series"] = df["SECURITY"].astype(str).tolist()
    data["Coupon"] = df["COUPON(%)"].astype(str).tolist()
    data["Maturity"] = df["MATURITY"].astype(str).tolist()
    data["YTM"] = df["BID START"].astype(str).tolist()
    data["Value"] = df["ISIN"].astype(str).tolist()

def TaxBonds():
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\TaxFree.csv'))
    data = dict()
    data["Symbol"] = df["Symbol"].astype(str).tolist()
    data["Series"] = df["Series"].astype(str).tolist()
    data["Coupon"] = df["Coupon"].astype(str).tolist()
    data["Maturity"] = df["Maturity"].astype(str).tolist()
    data["YTM"] = df["YTM"].astype(str).tolist()
    data["Value"] = df["PresentValue"].astype(str).tolist()
    return data

def CorpBonds():
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Corp_Bank_PSU.csv'))
    data = dict()
    data["Symbol"] = df["Fund Name"].astype(str).tolist()
    data["Coupon"] = df["Returns"].astype(str).tolist()
    data["Maturity"] = df["Rating"].astype(str).tolist()
    data["YTM"] = df["Risk"].astype(str).tolist()
    data["Value"] = df["Latest NAV"].astype(str).tolist()
    return data


def Coupon(request,amount,rate,period,frequency,number):
    x = list()
    rate = float(rate)
    year_coupon = amount * rate / 100
    coupon = (year_coupon * frequency / 12) * number
    x.append("Coupon Payment of Rs. {:1.2f} every {:2d} months".format(coupon, frequency))
    total_coupon = (year_coupon * period / 12) * number
    x.append("Total Coupon over the maturity: Rs. {:1.2f}".format(total_coupon))
    data = dict()
    data['Answer'] = x
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

