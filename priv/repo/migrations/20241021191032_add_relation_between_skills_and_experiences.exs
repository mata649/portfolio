defmodule Portfolio.Repo.Migrations.AddRelationBetweenSkillsAndExperiences do
  use Ecto.Migration

  def change do
    create table("skills_experiences", primary_key: false) do
      add :skill_id, references(:skills)
      add :experience_id, references(:experiences)
    end
  end
end
