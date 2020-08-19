from django.http import HttpResponse
import json
import requests
from bs4 import BeautifulSoup

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
