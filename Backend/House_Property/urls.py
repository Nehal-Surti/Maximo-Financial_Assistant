from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('<int:bhk>/<str:location>/<int:price>/',views.index, name='index')
]