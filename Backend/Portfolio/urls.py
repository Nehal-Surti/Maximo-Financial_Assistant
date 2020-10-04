from django.urls import path

from . import views

urlpatterns = [
    path('<int:tenure>/<int:goal_amount>/<int:salary>/<int:annual_investment>/<int:rd_rate>/<int:risk_tolerance>',views.index, name='index'),
]