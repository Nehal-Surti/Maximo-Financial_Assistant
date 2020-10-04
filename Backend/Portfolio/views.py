from django.shortcuts import render
from django.http import HttpResponse
import json
import pandas as pd
import requests
from statsmodels.tsa.arima_model import ARIMA
from bs4 import BeautifulSoup
import warnings
from sklearn.linear_model import LinearRegression
import numpy as np
from sklearn.preprocessing import PolynomialFeatures
from datetime import datetime
warnings.filterwarnings("ignore")
import pulp as p
import statsmodels.api as sm
import os
workpath = os.path.dirname(os.path.abspath(os.path.dirname(__package__)))
choices = [[["debt-liquid_combined"],["debt-low-duration_combined"],["hybrid_combined","debt-credit-risk_combined"]],[["debt-short-duration_3_more","debt-liquid_combined"],["advantage_3_more"],["equity-multi-cap_3_more"]],[["equity-large-cap_3_more"],["equity-multi-cap_3_more"],["equity-mid-cap_3_more","equity-small-cap_3_more"]]]
# Create your views here.
def index(request,tenure,goal_amount,salary,annual_investment,rd_rate,risk_tolerance):
    inflation_avg = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Inflation.csv'), usecols=['inflation forecast'])
    inf_dict = inflation_avg.to_dict()
    inflation_dict = inf_dict['inflation forecast']
    year = tenure + 2020
    indx = year - 2020
    rate = inflation_dict[indx]
    future_value = goal_amount * (1 + (rate / 100)) ** tenure
    if annual_investment==0:
        if salary < 150000:
            annual_investment = 0.05 * salary
        elif salary < 600000:
            annual_investment = 0.1 * salary
        elif salary < 1200000:
            annual_investment = 0.15 * salary
        elif salary < 1800000:
            annual_investment = 0.2 * salary
        else:
            annual_investment = 0.3 * salary
    corpus = annual_investment + (annual_investment * rd_rate * 6.5) / (100 * 12)
    annuity = round((future_value * (rate / 100)) / ((1 + (rate / 100)) ** tenure - 1))
    diff = annuity - annual_investment
    expected_rate = (annuity / annual_investment) ** (1 / tenure) - 1
    expected_rate = round(expected_rate * 100, 2)
    recommend_df = pd.DataFrame([['FD', 4, 1],
                                 ['bonds', 7, 2],
                                 ['MF', 12, 3],
                                 ['gold', 18, 4],
                                 ['Equity', 20, 5]])
    weight = []
    for i in recommend_df.index:
        if recommend_df[1][i] < expected_rate and recommend_df[2][i] <= risk_tolerance:
            weight.append(1)
        if recommend_df[1][i] < expected_rate and recommend_df[2][i] > risk_tolerance:
            weight.append(0)
        if recommend_df[1][i] > expected_rate and recommend_df[2][i] <= risk_tolerance:
            weight.append(2)
        if recommend_df[1][i] > expected_rate and recommend_df[2][i] > risk_tolerance:
            weight.append(0)
    tot = sum(weight)
    division = []
    part = annual_investment / tot
    for i in weight:
        division.append(part * i)
    data = dict()
    fd = list()
    for i in range(len(division)):
        if i==0 and division[i]!='0.0':
            fd.append(" Invest " + str(division[i]) + " in Fixed Deposits for " + str(tenure) + " years.")
            # data["Fixed Deposits"] = fd
        if i==3 and division[i]!='0.0':
            fd.append(GoldInvestment(division[i],tenure))
            # data["Gold"] = GoldInvestment(division[i],tenure)
        if i==1 and division[i]!='0.0':
            fd.append(Bonds(tenure,division[i],risk_tolerance))
            # data["Bonds"] = Bonds(tenure,division[i],risk_tolerance)
        if i==2 and division[i]!='0.0':
            fd.append(MutualFunds(tenure,division[i],risk_tolerance))
            # data["Mutual"] = MutualFunds(tenure,division[i],risk_tolerance)
        if i==4 and division[i]!='0.0':
            fd.append(Stocks(division[i],tenure))
            # data["Stocks"] = Stocks(division[i],tenure)
    data["Answer"] = fd
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result


def maturityCalculator(amount, rate, tenure):
    i = rate / 1200
    n = 12 * tenure

    maturity = (amount * ((1 + i) ** n - 1) / i) * (1 + i)

    return maturity


def getRiskDuration(risk, tenure):
    normalised_risk = 0
    normalised_tenure = 0

    if risk > 4:
        normalised_risk = 0
    elif risk >= 3:
        normalised_risk = 1
    else:
        normalised_risk = 2

    if tenure > 5:
        normalised_tenure = 2
    elif tenure >= 3:
        normalised_tenure = 1
    else:
        normalised_tenure = 0

    return normalised_risk, normalised_tenure


def getPeriodColumn(tenure):
    col = "1Yr"
    if tenure >= 20:
        col = "20Yr"
    elif tenure >= 15:
        col = "15Yr"
    elif tenure >= 10:
        col = "10Yr"
    elif tenure >= 5:
        col = "5Yr"
    elif tenure >= 3:
        col = "3Yr"

    return col

def MutualFunds(tenure,amount,risk):
    data = list()
    print(tenure)
    col, row = getRiskDuration(risk, tenure)
    choice = choices[row][col]
    rate_col = getPeriodColumn(tenure)
    file = 'Backend\\templates\\mutual\\'+choice[0]+'.csv'
    df = pd.read_csv(os.path.join(workpath, file))
    print(choice[0])
    df = df.sort_values(by=rate_col, ascending=False)
    best_rate = df.iloc[0][rate_col]
    maturity_amount = maturityCalculator(amount, best_rate, tenure)
    after_tax_gains = (maturity_amount - amount) * 0.8
    temp = " Invest " + str(amount) + " in Mutual Funds of " + str(df.iloc[0].to_numpy()[0]) +" at rate of " +str(best_rate) + " for " + str(tenure) + " years and get return "
    temp = temp + str(amount+after_tax_gains) + "."
    data.append(temp)
    return temp

def arima_predict(days, name):
  file = 'Backend\\templates\\content\\' + name + '.csv'
  df = pd.read_csv(os.path.join(workpath, file), usecols=['Date','Close'])
  forecast_out = days # Number of how many days to forecast
  df['Date'] = pd.to_datetime(df.Date, format='%d-%m-%Y')
  model = ARIMA(df['Close'], order=(5,1,0))
  result = model.fit()
  forecast = result.predict(start = len(df), end = (len(df)-1) + forecast_out, typ = 'levels').rename('Forecast')
  return list(forecast)[-1]


def Stocks(invest,tenure):
    days = tenure * 365
    stock_list = ['icici', 'hdfc', 'bajaj', 'cipla', 'gail', 'hul', 'itc', 'ongc', 'tcs', 'titan']
    current_price = []
    arima_price = []
    val = max(invest / 5000, 40)

    for i in stock_list:
        file = 'Backend\\templates\\content\\' + i + '.csv'
        df = pd.read_csv(os.path.join(workpath, file))
        current_price.append(list(df['Close'])[-1])
        arima_price.append(abs(arima_predict(days, i)))

    result = pd.DataFrame(np.array([stock_list, current_price, arima_price]).transpose())
    result.columns = ['stock', 'current', 'arima']
    result[['current', 'arima']] = result[['current', 'arima']].apply(pd.to_numeric)
    result['rate'] = (result['arima'] - result['current']) * 100 / result['current']
    result.sort_values(["rate"], axis=0, ascending=False, inplace=True)
    final_df = result[0:5]
    cons = list(final_df['current'])
    obj = list(final_df['arima'])
    stock = list(final_df['stock'])
    Lp_prob = p.LpProblem('Problem', p.LpMaximize)
    x = p.LpVariable("x", lowBound=0, cat='Integer')
    y = p.LpVariable("y", lowBound=0, cat='Integer')
    z = p.LpVariable("z", lowBound=0, cat='Integer')
    a = p.LpVariable("a", lowBound=0, cat='Integer')
    b = p.LpVariable("b", lowBound=0, cat='Integer')

    Lp_prob += obj[0] * x + obj[1] * y + obj[2] * z + obj[3] * a + obj[4] * b
    Lp_prob += cons[0] * x + cons[1] * y + cons[2] * z + cons[3] * a + cons[4] * b <= invest
    Lp_prob += x <= val
    Lp_prob += z <= val
    Lp_prob += y <= val
    Lp_prob += a <= val
    Lp_prob += b <= val

    status = Lp_prob.solve()
    return_value = p.value(Lp_prob.objective)
    number = [p.value(x), p.value(y), p.value(z), p.value(a), p.value(b)]
    final_df['Number'] = number
    final_df.query('Number > 0', inplace=True)
    print("Return value:", return_value)
    data = list()
    temp = ""
    for i in range(5):
        if number[i] != '0.0':
            temp = temp + " Invest " + str(current_price[i]) + " in " + str(number[i]) + " stocks of " + str(stock_list[i]).upper() + " for " + str(tenure) + " years."
    temp = temp + " Get a return of total of " + str(return_value)
    data.append(temp)
    return temp


def Bonds(tenure,amount,risk):
    data = list()
    temp = ""
    for i in range(5):
        if i==0:
            temp2 = get_GSec(tenure,amount,15)
            temp = '\n'.join([temp, temp2])
        if i==1:
            temp2 = get_TaxFree(tenure,amount)
            temp = '\n'.join([temp, temp2])
        if i==2:
            temp2 = calc_coupon_and_maturity_rbi(amount,15)
            temp = '\n'.join([temp, temp2])
        if i==3:
            temp2 = calc_coupon_and_maturity_gold(tenure,amount,15,6,2.75)
            temp = '\n'.join([temp, temp2])
        if i==4:
            temp2 = get_CorpBonds(risk,tenure,amount)
            temp = '\n'.join([temp, temp2])
    data.append(temp)
    return temp


def get_TaxFree(tenure, amount):
    if tenure < 10:
        return " No Tax-Free Bonds were found favorable to you."
    '''
    Get the Best Tax Free Bond Alternative

    tenure : Tenure in Years (10 Years or more)
    amount: Invested Amount (> 1,000 and multiples of 1,000)
    tax_rate: Tax Slab of user (5,10,15,20,30)%
    '''
    now = datetime.now()
    desired_year = now.year + tenure
    taxfree = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\TaxFree.csv'))
    df = taxfree.copy()
    units = amount / 1000
    for i, row in taxfree.iterrows():
        if row["Maturity"] < desired_year:
            df.drop(i, axis=0, inplace=True)
    # No match found
    if len(df) == 0:
        return " No Tax-Free Bonds were found favorable to you."
    best_alter = df.sort_values(by="YTM").iloc[-1]
    bond_name = best_alter["Symbol"] + ":" + best_alter["Series"]
    loss_due_to_secodnary = units * (float(best_alter["PresentValue"]) - float(best_alter["FaceValue"]))
    half_year_int = amount * best_alter["Coupon"] / (2 * 100)
    total_coupon = half_year_int * 2 * tenure
    best_amount = amount + total_coupon - loss_due_to_secodnary
    temp = " Buy " + str(units) + " units of Tax-Free Bond named " + str(bond_name) + " with rate of " + str(best_alter["Coupon"]) + "% at " + str(best_alter["PresentValue"] * units) + " for " + str(best_alter["Maturity"])
    temp = temp + " years. The return will be " + str(best_amount) +"."
    return temp

def get_GSec(tenure, amount, tax_rate):
    '''
    Get the Best Governement Securities Alternatives

    tenure : Tenure in Years
    amount: Invested Amount (> 10,000 and multiples of 10,000)
    tax_rate: Tax Slab of user (5,10,15,20,30)%
    '''
    now = datetime.now()
    desired_year = now.year + tenure
    Gsec = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\GSec.csv'))
    df = Gsec.copy()
    for i, row in Gsec.iterrows():
        if row["MATURITY"] < desired_year:
            df.drop(i, axis=0, inplace=True)
    if len(df) == 0:
        return " No Gov-Sec Bonds were found favorable to you."
    best_alter = df.sort_values(by="COUPON(%)").iloc[-1]
    half_year_int = amount * best_alter["COUPON(%)"] / (2 * 100)
    total_coupon = half_year_int * 2 * tenure
    # Taxed
    taxed_coupon = total_coupon * (100 - tax_rate) / 100
    best_amount = amount + taxed_coupon
    temp = " Invest " + str(amount) + " in Gov-Sec Bond with SYMBOL " + str(best_alter["SYMBOL"]) + " and ISIN " + str(best_alter["ISIN"])+ " with rate " + str(best_alter["COUPON(%)"]) + "% for " + str(best_alter["MATURITY"])
    temp = temp + " years. The return will be " + str(best_amount) + ". The BID-WINDOW is from " + str(best_alter["BID START"]) + "."
    return temp


def calc_coupon_and_maturity_rbi(amount, tax_rate, interest_rate=7.15, tenure=7):
    '''
    tenure : Tenure in Years (7 Years)
    amount: Invested Amount (> 10,000 and multiples of 10,000)
    tax_rate: Tax Slab of user (5,10,15,20,30)%
    '''
    half_year_coupon = amount * interest_rate / (2 * 100)
    total_coupon_amount = half_year_coupon * 2 * tenure
    # Tax deducted
    post_tax_coupon = total_coupon_amount * (100 - tax_rate) / 100
    best = amount + post_tax_coupon
    temp = " Invest " + str(amount) +" in RBI Bonds with rate at 7.15% for 7 years."
    temp = temp + " The return will be " + str(best) + "."
    return temp


def calc_coupon_and_maturity_gold(tenure,amount, tax_rate, rate=6, coupon_rate=2.75):
    '''
    tenure: Max 8 Years (5-8 Years)
    units: Amount of Bond Units
    ** rate: Rate of growth of Gold Prices assumed to be 6%
    per_unit: Per gram price
    coupon_rate: 2.75%
    '''
    half_year_coupon = amount * coupon_rate / (2 * 100)
    total_coupon = half_year_coupon * 2 * tenure
    maturity_amount = amount * ((1 + rate / 100) ** (tenure))
    total_returns = maturity_amount + total_coupon
    # taxed deducted
    total_coupon = (100 - tax_rate) * total_coupon / 100
    best = str(amount * ((1 + 10 / 100) ** (tenure)) + total_coupon)
    temp = " Invest" + str(amount) +"in Gold Bonds with rate 2.75% for " + str(tenure) + " years."
    temp = temp + " The returns will be " + str(best) + "."
    return temp


def get_CorpBonds(risk, tenure, amount):
    '''
    rate: desired rate
    risk: risk as per riskmap V
    tenure: tenure of investment
    '''
    riskmap = {"Low": 1, "Below Average": 2, "Average": 3, "Above Average": 4 }
    corp_bonds = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\corp_bond_funds.csv'))
    threshold = 1
    if tenure >= 20:
        threshold = 20
    elif tenure >= 15:
        threshold = 15
    elif tenure >= 10:
        threshold = 10
    elif tenure >= 5:
        threshold = 5
    elif tenure >= 3:
        threshold = 3
    else:
        threshold = 1
    df = corp_bonds.copy()
    rate_col = str(threshold) + "Yr"
    for i, row in corp_bonds.iterrows():
        if riskmap[row["Risk"]] <= risk:
            continue
        else:
            df.drop(index=i, inplace=True)
    if len(df) == 0:
        print("No match Found !")
        return " No Corporate Bonds found favorable for you."
    df = df.sort_values(by=rate_col)
    # best bond according the returns
    best_alter = df.iloc[-1]
    best_rate = best_alter[rate_col]
    best_returns = 0
    if best_rate.replace(".", "").isnumeric():
        best_returns = amount*((1+float(best_rate)/100)**(tenure))
    temp = " Buy " + str(best_alter["Fund Name"]) + " with expected future value of " + str(best_returns) + " at growth rate of " + str(best_alter[rate_col]) + "% after " + str(tenure) + " years."
    return temp

def GoldInvestment(amount,tenure):
    today = list()
    URL = "https://www.google.com/search?sxsrf=ALeKk02LRi6GYrfd6lf09UuibKJJu-SuwA%3A1599479462354&ei=ph5WX9WQFbGW4-EPyNuNoA0&q=gold+price&oq=gold+price&gs_lcp=CgZwc3ktYWIQAzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQRzIECAAQR1AAWABghO0KaABwAXgAgAEAiAEAkgEAmAEAqgEHZ3dzLXdpesABAQ&sclient=psy-ab&ved=0ahUKEwjV-7ap_dbrAhUxyzgGHchtA9QQ4dUDCA0&uact=5"
    r = requests.get(URL)
    soup = BeautifulSoup(r.content, "html5lib")
    table = soup.findAll('div', attrs={'class': "BNeawe deIvCb AP7Wnd"})
    temp = str(table[1]).split(":")[1].split(" ")[1].replace(",", "")
    today.append(float(temp))
    time_t = "2021-09-22"
    d = time_t.split("-")[0] + "-" + time_t.split("-")[1] + "-" + "01"
    s = "Price"
    year = int(time_t.split("-")[0])
    cols = ["Inflation", "USD"]
    df = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Gold.csv'))
    df = df.drop(columns=cols)
    df = df.dropna()
    df['Date'] = pd.to_datetime(df['Date'])
    df = df.set_index('Date')
    y = df[s].resample('MS').mean()
    mod = sm.tsa.statespace.SARIMAX(y, order=(1, 0, 0), enforce_stationarity=False, enforce_invertibility=False)
    results = mod.fit()
    pred = results.get_prediction(start=pd.to_datetime('2020-01-01'), end=pd.to_datetime(d), dynamic=False)
    forcast = pd.to_datetime(d)
    forecast = pred.predicted_mean[forcast]
    ef = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Gold.csv'))
    ef = ef.dropna()
    X = ef.Date
    Y = []
    inflation = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\Inflation.csv'))
    usd = pd.read_csv(os.path.join(workpath, 'Backend\\templates\\dataset\\USD.csv'))
    for i in X:
        b_date = datetime.strptime(i, '%Y-%m-%d')
        temp = (datetime.today() - b_date).days / 365
        Y.append(round(temp, 1))
    X1 = ef.drop(columns=['Price', 'Date'])
    Y1 = ef[s]
    polynomial_features = PolynomialFeatures(degree=2)
    x_poly = polynomial_features.fit_transform(X1)
    model = LinearRegression()
    model.fit(x_poly, Y1)
    investYear = 1.0
    for i in range(tenure):
        inf = inflation[inflation['Year'] == year]['inflation forecast'].to_numpy()[0]
        if year > 2025:
            dollar = 461.93
        else:
            dollar = usd[usd['Date'] == time_t]['USD'].to_numpy()[0]
        x_poly_test = polynomial_features.fit_transform(np.array([inf, dollar]).reshape(1, -1))
        poly_pred = model.predict(x_poly_test)
        poly_pred[0] = (poly_pred[0] * 10) / 31.1034768
        today.append(round(0.2 * forecast + 0.2 * poly_pred[0], 4))
        investYear = investYear + 1
        year = year + 1
        time_t = str(year) + "-" + time_t.split("-")[1] + "-" + time_t.split("-")[2]
    data = list()
    start = 2020
    gold = 0
    temp = ""
    for i in range(tenure):
        w = (amount * 10) / today[i]
        f = (today[i + 1] * w) / 10
        gold = gold + f
        temp2 = "Buy " + str(round(w,2)) + " grams of 24 carat Gold for " + str(amount) + " in " + str(start+i) + " and it will cost " + str(round(f,4)) + " after one year."
        temp = '\n'.join([temp, temp2])
    temp2 = "You will be able to collect " + str(gold) + " after investing in gold sector for " + str(tenure) + "years."
    temp = '\n'.join([temp, temp2])
    data.append(temp)
    return temp