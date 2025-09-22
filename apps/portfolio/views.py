from django.views.generic import TemplateView, ListView
from django.core.paginator import Paginator
from apps.portfolio.models import Skill, SiteInfo, Experience, Project


# Create your views here.
class HomePageView(TemplateView):
    template_name = 'portfolio/home.html'

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['skills'] = Skill.objects.all().filter(display_first=True)
        context['site_info'] = SiteInfo.objects.first()
        context['experiences'] = Experience.objects.prefetch_related('skills').all().order_by('-start_date')
        projects = Project.objects.prefetch_related('skills').all().order_by('-year')
        page_number = self.request.GET.get('page', 1)
        projects_page = Paginator(projects, 3).get_page(page_number)
        context['projects_page_obj'] = projects_page
        return context


class ProjectFilterView(ListView):
    model = Project
    template_name = 'portfolio/partials/project_list.html'
    context_object_name = 'projects'
    paginate_by = 3

    def get_queryset(self):
        projects = Project.objects.all()
        skills_param = self.request.GET.get('skills')
        if skills_param:
            skill_list = skills_param.split(',')
            for skill in skill_list:
                projects = projects.filter(skills__name__iexact=skill)
        return projects.order_by('-year')


class ExperienceFilterView(ListView):
    model = Experience
    template_name = 'portfolio/partials/experience_list.html'
    context_object_name = 'experiences'

    def get_queryset(self):
        experiences = Experience.objects.all()
        skills_param = self.request.GET.get('skills')

        if skills_param:
            skill_list = skills_param.split(',')
            for skill in skill_list:
                experiences = experiences.filter(skills__name__iexact=skill)

        return experiences.order_by('-start_date')
