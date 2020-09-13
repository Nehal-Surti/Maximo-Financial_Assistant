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
    if id==3:
        data = HomeLoan()
    if id==4:
        data = CarLoan()
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

def CarLoan():
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\CarLoans.csv'))
    data = dict()
    data["BankName"] = df['BankName'].tolist()
    data["Rate"] = df['Rate'].tolist()
    data["Fix"] = df['Fix/Float'].tolist()
    data["maxTenure"] = df['TenureYrs'].tolist()
    data["LTV"] = df['MaxLoanAmt'].tolist()
    data["Road"] = df['Road/Show'].tolist()
    return data

def EducationLoan():
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Education_Loans.csv'))
    data = dict()
    data["BankName"] = df['BankName'].tolist()
    data["IndiaRate"] = df['IndiaRate'].tolist()
    data["AbroadRate"] = df['AbroadRate'].tolist()
    return data

def HomeLoan():
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\HomeLoans.csv'))
    data = dict()
    data["BankName"] = df["BankName"].astype(str).tolist()
    data["Fee"] = df["ProcessingFee"].astype(str).tolist()
    data["Rate"] = df["Rate"].astype(str).tolist()
    data["maxTenure"] = df["MaxTenure"].astype(int).astype(str).tolist()
    data["maxAge"] = df["MaxAge"].astype(str).tolist()
    data["LTV"] = df["PropValue"].astype(str).tolist()
    return data

def getEMI(request,loan,interest,period):
    interestRate = float(interest)
    EMI = loan * (interestRate / 1200) * ((1 + interestRate / 1200) ** period) / (((1 + interestRate / 1200) ** period) - 1)
    data = dict()
    data["Answer"] = str(round(EMI,3))
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result


def EMI(request,amount,emi,rate,period):
    emi = float(emi)
    rate = float(rate)
    data = dict()
    interestPaid = []
    principalPaid = []

    AmmortizedSchedule = pd.DataFrame(
        columns=["Date", "Interest", "Principal", "Remaining Amount"])

    cutInterest = round(amount * (rate / 1200), 0)
    cutPrincipal = round(emi - cutInterest, 0)

    curr_date = date.today()

    ## Add year wise for longer loans
    for i in range(period):
        interestPaid.append(cutInterest)
        principalPaid.append(cutPrincipal)

        amount -= cutPrincipal

        AmmortizedSchedule.loc[len(AmmortizedSchedule)] = [curr_date, cutInterest, cutPrincipal, amount]
        curr_date = curr_date + relativedelta(months=+ 1)

        cutInterest = round(amount * (rate / 1200), 0)
        cutPrincipal = round(emi - cutInterest, 0)

    AmmortizedSchedule['Date'] = pd.to_datetime(AmmortizedSchedule['Date'])
    AmmortizedSchedule = AmmortizedSchedule.groupby(AmmortizedSchedule.Date.dt.year).max()
    data["Year"] = AmmortizedSchedule.Date.astype(str).tolist()
    data["Interest"] = AmmortizedSchedule["Interest"].astype(str).tolist()
    data["Principal"] = AmmortizedSchedule["Principal"].astype(str).tolist()
    data["Remaining"] = AmmortizedSchedule["Remaining Amount"].astype(str).tolist()
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

