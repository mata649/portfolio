defmodule Portfolio.Repo.Migrations.CreateExperiences do
  use Ecto.Migration

  def change do
    create table(:experiences) do
      add :company, :string
      add :position, :string
      add :description, :string
      add :from, :date
      add :to, :date
      add :currentJob, :boolean, default: false, null: false
      add :location, :string

      timestamps(type: :utc_datetime)
    end
  end
end
