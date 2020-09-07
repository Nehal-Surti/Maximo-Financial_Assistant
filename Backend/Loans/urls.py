from django.urls import path

from . import views

urlpatterns = [
    path('<int:id>/',views.index, name='index'),
    path('<int:amount>/<int:emi>/<int:rate>/<int:period>/',views.EMI, name='EMI'),
]