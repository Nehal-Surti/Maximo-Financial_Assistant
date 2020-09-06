from django.http import HttpResponse
import json
import pandas as pd
import os
from datetime import date
from dateutil.relativedelta import relativedelta
workpath = os.path.dirname(os.path.abspath(os.path.dirname(__package__)))

from django.shortcuts import render

# Create your views here.
def index(request,id):
    if id==5:
        data = EducationLoan()
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

def EducationLoan():
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Education_Loans.csv'))
    data = dict()
    data["BankName"] = df['BankName'].tolist()
    data["IndiaRate"] = df['IndiaRate'].tolist()
    data["AbroadRate"] = df['AbroadRate'].tolist()
    return data

def EMI(request,amount,emi,rate,period):
    data = dict()
    interestPaid = []
    principalPaid = []

    AmmortizedSchedule = pd.DataFrame(
        columns=["Date", "Interest", "Principal", "Remaining Amount"])

    cutInterest = round(amount * rate / 1200, 0)
    cutPrincipal = round(emi - cutInterest, 0)

    curr_date = date.today()

    ## Add year wise for longer loans
    for i in range(period):
        interestPaid.append(cutInterest)
        principalPaid.append(cutPrincipal)

        amount -= cutPrincipal

        AmmortizedSchedule.loc[len(AmmortizedSchedule)] = [curr_date, cutInterest, cutPrincipal, amount]
        curr_date = curr_date + relativedelta(months=+ 1)

        cutInterest = round(amount * rate / 1200, 0)
        cutPrincipal = round(emi - cutInterest, 0)

    AmmortizedSchedule['Date'] = pd.to_datetime(AmmortizedSchedule['Date'])
    AmmortizedSchedule = AmmortizedSchedule.groupby(AmmortizedSchedule.Date.dt.year).max()
    data["Year"] = AmmortizedSchedule.Date.astype(str).tolist()
    data["Interest"] = AmmortizedSchedule["Interest"].astype(str).tolist()
    data["Principal"] = AmmortizedSchedule["Principal"].astype(str).tolist()
    data["Remaining"] = AmmortizedSchedule["Remaining Amount"].astype(str).tolist()
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

