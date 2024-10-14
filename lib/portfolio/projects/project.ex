defmodule Portfolio.Projects.Project do
  use Ecto.Schema
  import Ecto.Changeset

  schema "projects" do
    field :name, :string
    field :description, :string
    field :githubURL, :string

    timestamps(type: :utc_datetime)
  end

  @doc false
  def changeset(project, attrs) do
    project
    |> cast(attrs, [:name, :description, :githubURL])
    |> validate_required([:name, :description, :githubURL])
    |> validate_length(:description, max: 255)
    |> validate_length(:name, max: 50)
    |> validate_format(:githubURL, ~r/^https:\/\/github\.com(?:\/[^\s\/]+){2}$/)
  end
end
