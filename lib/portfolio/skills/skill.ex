defmodule Portfolio.Skills.Skill do
  use Ecto.Schema
  import Ecto.Changeset

  schema "skills" do
    field :name, :string
    field :color, :string

    many_to_many :projects, Portfolio.Projects.Project,
      join_through: "skills_projects",
      on_delete: :delete_all

    many_to_many :experiences, Portfolio.Experiences.Experience,
      join_through: "skills_experiences",
      on_delete: :delete_all

    timestamps(type: :utc_datetime)
  end

  @doc false
  def changeset(skill, attrs) do
    skill
    |> cast(attrs, [:name, :color])
    |> validate_required([:name, :color])
    |> validate_length(:name, max: 30)
    |> validate_length(:color, is: 7)
    |> validate_format(:color, ~r/^#(?:[0-9a-fA-F]{3}){1,2}$/)
  end
end
