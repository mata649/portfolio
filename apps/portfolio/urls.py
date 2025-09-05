from django.urls import path
from . import views

urlpatterns = [
    path('', views.HomePageView.as_view(), name='home'),
    path('filter_projects/', views.ProjectFilterView.as_view(), name='filter_projects'),
    path('filter_experiences/', views.ExperienceFilterView.as_view(), name='filter_experiences'),
]
