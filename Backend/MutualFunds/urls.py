from django.urls import path

from . import views

urlpatterns = [
    path('<int:amount>/<str:risk>/',views.growth,name='Growth'),
    path('<int:amount>/<int:id>/',views.index,name='Index'),
]