from django.urls import path

from . import views

urlpatterns = [
    path('<int:days>/<str:name>/',views.index,name='Index'),
]