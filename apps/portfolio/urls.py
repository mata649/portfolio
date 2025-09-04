from django.urls import path
from . import views

urlpatterns = [
    path('', views.HomePageView.as_view(), name='home'),
    path('filter_projects/', views.filter_projects, name='filter_projects'),
]
