import os
from dotenv import load_dotenv

import os
from dotenv import load_dotenv


class Config:
    def __init__(self):
        load_dotenv()

        self.SECRET_KEY = self._get_required('DJANGO_SECRET_KEY')
        self.DOMAIN = self._get_required('DOMAIN')

        self.AWS_ACCESS_KEY_ID = self._get_required('AWS_ACCESS_KEY_ID')
        self.AWS_SECRET_ACCESS_KEY = self._get_required('AWS_SECRET_ACCESS_KEY')
        self.AWS_STORAGE_BUCKET_NAME = self._get_required('AWS_STORAGE_BUCKET_NAME')
        self.AWS_S3_REGION_NAME = self._get_required('AWS_S3_REGION_NAME')
        self.AWS_S3_CUSTOM_DOMAIN = self._get_required('AWS_S3_CUSTOM_DOMAIN')

        self.DB_USER = self._get_required('DJANGO_DB_USER')
        self.DB_PASSWORD = self._get_required('DJANGO_DB_PASSWORD')
        self.DB_HOST = self._get_required('DJANGO_DB_HOST')
        self.DB_PORT = self._get_required('DJANGO_DB_PORT')
        self.DB_NAME = self._get_required('DJANGO_DB_NAME')

    @staticmethod
    def _get_required(key: str) -> str:
        value = os.getenv(key)
        if not value:
            raise RuntimeError(f'{key} has not been set')
        return value

    def _is_any_db_var_empty(self) -> bool:
        return not (
                self.DB_USER
                and self.DB_PASSWORD
                and self.DB_HOST
                and self.DB_PORT
                and self.DB_NAME
        )

    @staticmethod
    def _is_prod_env() -> bool:
        settings_module = os.getenv('DJANGO_SETTINGS_MODULE')
        return bool(settings_module and settings_module.endswith('prod'))
