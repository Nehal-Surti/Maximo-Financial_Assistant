from django.urls import path

from . import views

urlpatterns = [
    path('<int:id>/',views.index, name='index'),
    path('<int:amount>/<float:emi>/<float:rate>/<int:period>/',views.EMI, name='EMI'),
]