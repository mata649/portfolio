from django.db import models


# Create your models here.
class Skill(models.Model):
    name = models.CharField(blank=False, max_length=30)
    display_first = models.BooleanField(default=False)
    def __str__(self):
        return self.name


class Project(models.Model):
    name = models.CharField(blank=False, max_length=128)
    description = models.TextField(blank=False)
    github_link = models.URLField(blank=False, max_length=255)
    skills = models.ManyToManyField(Skill, related_name='projects')
    year = models.PositiveSmallIntegerField(blank=False, default=0)

    def __str__(self):
        return self.name


class Experience(models.Model):
    position = models.CharField(blank=False, max_length=100)
    location = models.CharField(blank=False, max_length=100)
    company = models.CharField(blank=False, max_length=100)
    description = models.TextField(blank=False)
    start_date = models.DateField(blank=False)
    end_date = models.DateField(null=True, blank=True)
    is_current = models.BooleanField(default=False)
    skills = models.ManyToManyField(Skill, related_name='experiences')

    def __str__(self):
        return f'{self.position} - {self.company}'


class SiteInfo(models.Model):
    about_me = models.TextField(blank=False)
    github_link = models.URLField(blank=False, max_length=255)
    linkedin_link = models.URLField(blank=False, max_length=255)
    email = models.EmailField(blank=False, max_length=255)

    class Meta:
        verbose_name_plural = 'Site info'
