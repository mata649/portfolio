from django.contrib import admin

from apps.portfolio.models import Skill, Project, Experience, SiteInfo


# Register your models here.
@admin.register(Skill)
class SkillAdmin(admin.ModelAdmin):
    search_fields = ['name']
    list_display = ['name']


@admin.register(Project)
class ProjectAdmin(admin.ModelAdmin):
    search_fields = ['name']
    list_display = ['name']
    filter_horizontal = ['skills']


@admin.register(Experience)
class ExperienceAdmin(admin.ModelAdmin):
    list_display = ['position', 'company', 'start_date', 'end_date', 'is_current']
    search_fields = ['position', 'company']
    list_filter = ['is_current', 'start_date', 'end_date']


@admin.register(SiteInfo)
class SiteInfoAdmin(admin.ModelAdmin):
    def has_add_permission(self, request):
        return not SiteInfo.objects.exists()
