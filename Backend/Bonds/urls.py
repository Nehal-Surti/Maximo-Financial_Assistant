from django.urls import path

from . import views

urlpatterns = [
    path('<int:amount>/<str:rate>/<int:period>/<int:frequency>/',views.Coupon, name='Coupon')
]