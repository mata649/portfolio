import os
from dotenv import load_dotenv


class Config:
    def __init__(self):
        load_dotenv()
        self.SECRET_KEY = os.getenv('DJANGO_SECRET_KEY')
        if not self.SECRET_KEY:
            raise RuntimeError('The django secret key has not been set')

        self.DB_USER = os.getenv('DJANGO_DB_USER')
        self.DB_PASSWORD = os.getenv('DJANGO_DB_PASSWORD')
        self.DB_HOST = os.getenv('DJANGO_DB_HOST')
        self.DB_PORT = os.getenv('DJANGO_DB_PORT')
        self.DB_NAME = os.getenv('DJANGO_DB_NAME')
        if not self._is_dev():
            if self._is_any_db_var_empty():
                raise RuntimeError('None of the DB variables can be empty')

    def _is_any_db_var_empty(self):
        return (self.DB_USER
                and self.DB_PASSWORD
                and self.DB_HOST
                and self.DB_PORT
                and self.DB_NAME)

    @staticmethod
    def _is_dev() -> bool:
        settings = os.getenv('DJANGO_SETTINGS_MODULE')
        if not settings:
            raise RuntimeError('Setting module not specified')
        return 'prod' in settings
