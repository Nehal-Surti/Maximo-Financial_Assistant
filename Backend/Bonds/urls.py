from django.urls import path

from . import views

urlpatterns = [
    path('<int:id>/',views.index,name='Index'),
    path('<int:amount>/<str:rate>/<int:period>/<int:frequency>/<int:number>/',views.Coupon, name='Coupon')
]