defmodule Portfolio.Skills do
  @moduledoc """
  The Skills context.
  """

  import Ecto.Query, warn: false
  alias Portfolio.Repo

  alias Portfolio.Skills.Skill

  @doc """
  Returns the list of skills.

  ## Examples

      iex> list_skills()
      [%Skill{}, ...]

  """
  def list_skills(preloads \\ []) do
    Repo.all(Skill) |> Repo.preload(preloads)
  end

  @doc """
  Returns the list of skills by name.

  ## Examples

      iex> search_skills_by_name("C")
      [%Skill{}, ...]

  """
  def search_skills_by_name(name) do
    query = "%#{name}%"
    Repo.all(from s in Skill, where: like(s.name, ^query))
  end

  @doc """
  Gets a single skill.

  Raises `Ecto.NoResultsError` if the Skill does not exist.

  ## Examples

      iex> get_skill!(123)
      %Skill{}

      iex> get_skill!(456)
      ** (Ecto.NoResultsError)

  """
  def get_skill!(id, preloads \\ []), do: Repo.get!(Skill, id) |> Repo.preload(preloads)

  @doc """
  Creates a skill.

  ## Examples

      iex> create_skill(%{field: value})
      {:ok, %Skill{}}

      iex> create_skill(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_skill(attrs \\ %{}) do
    %Skill{}
    |> Skill.changeset(attrs)
    |> Repo.insert()
  end

  @doc """
  Updates a skill.

  ## Examples

      iex> update_skill(skill, %{field: new_value})
      {:ok, %Skill{}}

      iex> update_skill(skill, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_skill(%Skill{} = skill, attrs) do
    skill
    |> Skill.changeset(attrs)
    |> Repo.update()
  end

  @doc """
  Deletes a skill.

  ## Examples

      iex> delete_skill(skill)
      {:ok, %Skill{}}

      iex> delete_skill(skill)
      {:error, %Ecto.Changeset{}}

  """
  def delete_skill(%Skill{} = skill) do
    Repo.delete(skill)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking skill changes.

  ## Examples

      iex> change_skill(skill)
      %Ecto.Changeset{data: %Skill{}}

  """
  def change_skill(%Skill{} = skill, attrs \\ %{}) do
    Skill.changeset(skill, attrs)
  end

  def load_skills(%{"skills" => skills} = attrs) do
    skills = Repo.all(from s in Skill, where: s.id in ^skills)
    attrs |> Map.put("skills", skills)
  end

  def load_skills(%{skills: skills} = attrs) do
    skills = Repo.all(from s in Skill, where: s.id in ^skills)
    attrs |> Map.put(:skills, skills)
  end

  def load_skills(attrs) do
    attrs |> Map.put(:skills, [])
  end
end
