defmodule Portfolio.Repo.Migrations.CreateSkills do
  use Ecto.Migration

  def change do
    create table(:skills) do
      add :name, :string
      add :color, :string

      timestamps(type: :utc_datetime)
    end
  end
end
