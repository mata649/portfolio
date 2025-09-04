from django.shortcuts import render
from django.views.generic import TemplateView
from django.db.models import Q

from apps.portfolio.models import Skill, SiteInfo, Experience, Project


# Create your views here.
class HomePageView(TemplateView):
    template_name = 'portfolio/home.html'

    def get_context_data(self, **kwargs):
        context = super(HomePageView, self).get_context_data(**kwargs)
        context['skills'] = Skill.objects.all()
        context['site_info'] = SiteInfo.objects.first()
        context['projects'] = Project.objects.prefetch_related('skills').all()
        context['experiences'] = Experience.objects.all()
        return context


def filter_projects(request):
    skills_param = request.GET.get('skills')
    projects = Project.objects.all()
    if skills_param:
        skill_list = skills_param.split(',')
        for skill in skill_list:
            projects = Project.objects.filter(skills__name__iexact=skill)

    return render(request, 'portfolio/partials/project_list.html', {
        'projects': projects
    })
