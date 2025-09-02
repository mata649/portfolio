from django.db import models


# Create your models here.
class Skill(models.Model):
    name = models.CharField(blank=False, max_length=30)

    def __str__(self):
        return self.name


class Project(models.Model):
    name = models.CharField(blank=False, max_length=128)
    description = models.TextField(blank=False)
    github_link = models.URLField(blank=False, max_length=255)
    skills = models.ManyToManyField(Skill, related_name='projects')

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
