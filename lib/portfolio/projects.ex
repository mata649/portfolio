defmodule Portfolio.Projects do
  @moduledoc """
  The Projects context.
  """

  import Ecto.Query, warn: false
  alias Portfolio.Skills
  alias Portfolio.Repo

  alias Portfolio.Projects.Project

  @doc """
  Returns the list of projects.

  ## Examples

      iex> list_projects()
      [%Project{}, ...]

  """
  def list_projects(preloads \\ []) do
    Repo.all(Project) |> Repo.preload(preloads)
  end

  @doc """
  Gets a single project.

  Raises `Ecto.NoResultsError` if the Project does not exist.

  ## Examples

      iex> get_project!(123)
      %Project{}

      iex> get_project!(456)
      ** (Ecto.NoResultsError)

  """
  def get_project!(id, preloads \\ []), do: Repo.get!(Project, id) |> Repo.preload(preloads)

  @doc """
  Creates a project.

  ## Examples

      iex> create_project(%{field: value})
      {:ok, %Project{}}

      iex> create_project(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_project(attrs \\ %{}) do
    %Project{} |> Project.changeset(attrs |> Skills.load_skills()) |> Repo.insert()
  end

  @doc """
  Updates a project.

  ## Examples

      iex> update_project(project, %{field: new_value})
      {:ok, %Project{}}

      iex> update_project(project, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_project(%Project{} = project, attrs) do
    project
    |> Project.changeset(attrs |> Skills.load_skills())
    |> Repo.update()
  end

  @doc """
  Deletes a project.

  ## Examples

      iex> delete_project(project)
      {:ok, %Project{}}

      iex> delete_project(project)
      {:error, %Ecto.Changeset{}}

  """
  def delete_project(%Project{} = project) do
    Repo.delete(project)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking project changes.

  ## Examples

      iex> change_project(project)
      %Ecto.Changeset{data: %Project{}}

  """
  def change_project(%Project{} = project, attrs \\ %{}) do
    Project.changeset(project, attrs)
  end

  @doc """
  Fetches all projects that have the specified skills.

  ## Parameters

    - skills: A list of skill IDs to filter projects by.

  ## Examples

      iex> get_projects_by_skills([1, 2, 3])
      [%Project{}, %Project{}]

  Returns a list of projects that have the specified skills.

  """
  def get_projects_by_skills([], params) do
    query =
      from p in Project,
        join: s in assoc(p, :skills),
        distinct: true,
        preload: [:skills]

    Flop.validate_and_run(query, params, repo: Portfolio.Repo)
  end

  def get_projects_by_skills(skills, params) do
    query =
      from p in Project,
        join: s in assoc(p, :skills),
        distinct: true,
        preload: [:skills]

    query = from [p, s] in query, where: s.id in ^skills

    Flop.validate_and_run(query, params, repo: Portfolio.Repo)
  end
end
