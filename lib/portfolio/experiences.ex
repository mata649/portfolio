defmodule Portfolio.Experiences do
  @moduledoc """
  The Experiences context.
  """

  import Ecto.Query, warn: false
  alias Portfolio.Skills
  alias Portfolio.Repo

  alias Portfolio.Experiences.Experience

  @doc """
  Returns the list of experiences.

  ## Examples

      iex> list_experiences()
      [%Experience{}, ...]

  """
  def list_experiences(preloads \\ []) do
    Repo.all(Experience) |> Repo.preload(preloads)
  end

  @doc """
  Gets a single experience.

  Raises `Ecto.NoResultsError` if the Experience does not exist.

  ## Examples

      iex> get_experience!(123)
      %Experience{}

      iex> get_experience!(456)
      ** (Ecto.NoResultsError)

  """
  def get_experience!(id, preloads \\ []), do: Repo.get!(Experience, id) |> Repo.preload(preloads)

  @doc """
  Creates a experience.

  ## Examples

      iex> create_experience(%{field: value})
      {:ok, %Experience{}}

      iex> create_experience(%{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def create_experience(attrs \\ %{}) do
    %Experience{}
    |> Experience.changeset(attrs |> Skills.load_skills())
    |> Repo.insert()
  end

  @doc """
  Updates a experience.

  ## Examples

      iex> update_experience(experience, %{field: new_value})
      {:ok, %Experience{}}

      iex> update_experience(experience, %{field: bad_value})
      {:error, %Ecto.Changeset{}}

  """
  def update_experience(%Experience{} = experience, attrs) do
    experience
    |> Experience.changeset(attrs |> Skills.load_skills())
    |> Repo.update()
  end

  @doc """
  Deletes a experience.

  ## Examples

      iex> delete_experience(experience)
      {:ok, %Experience{}}

      iex> delete_experience(experience)
      {:error, %Ecto.Changeset{}}

  """
  def delete_experience(%Experience{} = experience) do
    Repo.delete(experience)
  end

  @doc """
  Returns an `%Ecto.Changeset{}` for tracking experience changes.

  ## Examples

      iex> change_experience(experience)
      %Ecto.Changeset{data: %Experience{}}

  """
  def change_experience(%Experience{} = experience, attrs \\ %{}) do
    Experience.changeset(experience, attrs)
  end

  @doc """
  Fetches all experiences that have the specified skills.

  ## Parameters

    - skills: A list of skill IDs to filter experiences by. If an empty list is provided, all experiences are returned.

  ## Examples

      iex> get_experiences_by_skills([1, 2, 3])
      [%Experience{}, %Experience{}]

      iex> get_experiences_by_skills([])
      [%Experience{}, %Experience{}]

  Returns a list of experiences that have the specified skills or all experiences if no skills are specified.

  """
  def get_experiences_by_skills() do
    Repo.all(
      from e in Experience,
        join: s in assoc(e, :skills),
        distinct: true,
        preload: [:skills],
        order_by: [desc: e.from]
    )
  end
end
