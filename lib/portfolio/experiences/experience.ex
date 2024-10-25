defmodule Portfolio.Experiences.Experience do
  use Ecto.Schema
  import Ecto.Changeset

  schema "experiences" do
    field :position, :string
    field :description, :string
    field :location, :string
    field :to, :date
    field :from, :date
    field :company, :string
    field :current_job, :boolean, default: false

    many_to_many :skills, Portfolio.Skills.Skill,
      join_through: "skills_experiences",
      on_replace: :delete,
      on_delete: :delete_all

    timestamps(type: :utc_datetime)
  end

  @doc false
  def changeset(experience, attrs) do
    skills = get_skills(attrs)

    experience
    |> cast(attrs, [:company, :position, :description, :from, :to, :current_job, :location])
    |> validate_required([:company, :position, :description, :from, :current_job, :location])
    |> validate_to_date()
    |> validate_length(:position, max: 50)
    |> validate_length(:description, max: 500)
    |> validate_length(:location, max: 50)
    |> validate_length(:company, max: 50)
    |> put_assoc(:skills, skills, required: true)
    |> validate_length(:skills, min: 1)
  end

  def validate_to_date(changeset) do
    cond do
      get_field(changeset, :current_job) && get_field(changeset, :to) != nil ->
        changeset |> add_error(:to, "To can't be set if current job is true")

      get_field(changeset, :current_job) == false && get_field(changeset, :to) == nil ->
        changeset |> add_error(:to, "To can't be nil if current job is false")

      true ->
        changeset
    end
  end

  defp get_skills(%{"skills" => skills}), do: skills
  defp get_skills(%{skills: skills}), do: skills
  defp get_skills(_attrs), do: []
end
