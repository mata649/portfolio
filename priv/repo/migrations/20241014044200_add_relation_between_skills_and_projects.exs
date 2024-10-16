defmodule Portfolio.Repo.Migrations.AddRelationBetweenSkillsAndProjects do
  use Ecto.Migration

  def change do
    create table("skills_projects", primary_key: false) do
      add :skill_id, references(:skills)
      add :project_id, references(:projects)
    end
  end
end
