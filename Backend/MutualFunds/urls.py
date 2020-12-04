from django.urls import path

from . import views

urlpatterns = [
    path('<int:amount>/<str:risk>/',views.growth,name='Growth'),
    path('<int:amount>/<int:id>/<int:nogrowth>/',views.index,name='Index'),
]