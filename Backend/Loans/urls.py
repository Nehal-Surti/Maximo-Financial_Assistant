from django.urls import path

from . import views

urlpatterns = [
    path('<int:id>/',views.index, name='index'),
    path('<int:loan>/<str:interest>/<int:period>/', views.getEMI, name='getEMI'),
    path('<int:amount>/<str:emi>/<str:rate>/<int:period>/',views.EMI, name='EMI'),
]