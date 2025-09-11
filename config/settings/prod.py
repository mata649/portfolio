from .base import *
from storages.backends.s3boto3 import S3Boto3Storage

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = config.SECRET_KEY

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = False

INSTALLED_APPS += ['storages']

ALLOWED_HOSTS = ['*']
CSRF_TRUSTED_ORIGINS = [f'https://*.{config.DOMAIN}']
# Database
# https://docs.djangoproject.com/en/5.2/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql',
        'NAME': config.DB_NAME,
        'USER': config.DB_USER,
        'PASSWORD': config.DB_PASSWORD,
        'HOST': config.DB_HOST,
        'PORT': config.DB_PORT,
    }
}

AWS_ACCESS_KEY_ID = config.AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY = config.AWS_SECRET_ACCESS_KEY
AWS_STORAGE_BUCKET_NAME = config.AWS_STORAGE_BUCKET_NAME
AWS_S3_REGION_NAME = config.AWS_S3_REGION_NAME
AWS_S3_CUSTOM_DOMAIN = config.AWS_S3_CUSTOM_DOMAIN
AWS_S3_SIGNATURE_VERSION = 's3v4'
AWS_S3_FILE_OVERWRITE = True
AWS_LOCATION = 'static'
STATICFILES_STORAGE = 'storages.backends.s3boto3.S3StaticStorage'
STATIC_ROOT = BASE_DIR / 'staticfiles'
STORAGES = {
    'default': {
        'BACKEND': 'storages.backends.s3boto3.S3StaticStorage',
    },

    'staticfiles': {
        'BACKEND': 'storages.backends.s3boto3.S3StaticStorage',
    },
}

STATIC_URL = f'https://{AWS_S3_CUSTOM_DOMAIN}/static/'
