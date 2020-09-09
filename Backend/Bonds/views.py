import json

from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.

def Coupon(request,amount,rate,period,frequency):
    x = list()
    rate = float(rate)
    year_coupon = amount * rate / 100
    coupon = year_coupon * frequency / 12
    x.append("Coupon Payment of Rs. {:1.2f} every {:2d} months".format(coupon, frequency))
    total_coupon = year_coupon * period / 12
    x.append("Total Coupon over the maturity: Rs. {:1.2f}".format(total_coupon))
    data = dict()
    data['Answer'] = x
    result = HttpResponse(json.dumps(data, ensure_ascii=False), content_type="application/json")
    return result

