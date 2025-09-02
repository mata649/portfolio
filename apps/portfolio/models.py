from django.db import models


# Create your models here.
class Skill(models.Model):
    name = models.CharField(blank=False, max_length=30)

class Project(models.Model):
    name = models.CharField(blank=False, max_length=128)
    short_description = models.CharField(blank=False, max_length=512)
    github_link = models.URLField(blank=False, max_length=255)
    skills = models.ManyToManyField(Skill, related_name='projects')


class Experience(models.Model):
    position = models.CharField(blank=False, max_length=100)
    location = models.CharField(blank=False, max_length=100)
    company = models.CharField(blank=False, max_length=100)
    description = models.TextField(blank=False)
    start_date = models.DateField(blank=False)
    end_date = models.DateField(null=True, blank=True)
    is_current = models.BooleanField(default=False)
    skills = models.ManyToManyField(Skill, related_name='experiences')
