from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('<int:weight>/<str:time_t>/<int:today>/',views.prediction, name='prediction')
]