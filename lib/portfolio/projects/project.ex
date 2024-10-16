defmodule Portfolio.Projects.Project do
  alias Portfolio.Skills.Skill
  use Ecto.Schema
  import Ecto.Changeset

  schema "projects" do
    field :name, :string
    field :description, :string
    field :githubURL, :string
    many_to_many :skills, Skill, join_through: "skills_projects", on_replace: :delete
    timestamps(type: :utc_datetime)
  end

  @doc false
  def changeset(project, attrs) do
    skills = attrs["skills"]

    project
    |> cast(attrs, [:name, :description, :githubURL])
    |> validate_required([:name, :description, :githubURL])
    |> validate_length(:description, max: 255)
    |> validate_length(:name, max: 50)
    |> validate_format(:githubURL, ~r/^https:\/\/github\.com(?:\/[^\s\/]+){2}$/)
    |> put_assoc(:skills, skills, required: true)
    |> validate_length(:skills, min: 1)
  end
end
