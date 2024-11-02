defmodule PortfolioWeb.Home do
  alias Portfolio.Experiences
  alias Portfolio.Projects
  alias Portfolio.Skills
  use PortfolioWeb, :live_view

  def mount(_params, _session, socket) do
    skills = Skills.list_skills()
    middle_index = ceil(length(skills) / 2)
    skills_first_half = skills |> Enum.slice(0..(middle_index - 1)) |> Enum.with_index()
    skills_second_half = skills |> Enum.slice(middle_index..length(skills)) |> Enum.with_index()

    {projects, projects_meta} = get_projects()
    experiences = Experiences.get_experiences_by_skills()

    socket =
      socket
      |> assign(:skills, skills)
      |> assign(:skills_first_half, skills_first_half)
      |> assign(:skills_second_half, skills_second_half)
      |> assign(:skills_filter, [])
      |> assign(:projects, projects)
      |> assign(:projects_meta, projects_meta)
      |> assign(:experiences, experiences)
      |> assign(:loading?, false)

    {:ok, socket}
  end

  def handle_event("add_skill_filter", %{"id" => id}, socket) do
    send(self(), {:add_skill_filter, id})

    {:noreply, socket |> assign(:loading?, true)}
  end

  def handle_event("change_page", %{"page" => page}, socket) do
    send(self(), {:change_page, page})
    {:noreply, socket |> assign(:loading?, true)}
  end

  def handle_info({:change_page, page}, socket) do
    skills_filter = socket.assigns.skills_filter

    {projects, projects_meta} = get_projects(skills_filter, %Flop{page_size: 3, page: page})

    socket =
      socket
      |> assign(:projects, projects)
      |> assign(:projects_meta, projects_meta)
      |> assign(:loading?, false)

    {:noreply, socket}
  end

  def handle_info({:add_skill_filter, id}, socket) do
    skills_filter = socket.assigns.skills_filter
    :timer.sleep(400)

    skills_filter = skills_filter |> toggle_skill_id_from_list(id)

    {projects, projects_meta} = get_projects(skills_filter)

    socket =
      socket
      |> assign(:skills_filter, skills_filter)
      |> assign(:projects, projects)
      |> assign(:projects_meta, projects_meta)
      |> assign(:loading?, false)

    {:noreply, socket}
  end

  defp get_projects(skills_filter \\ [], flop_params \\ %Flop{page_size: 3}) do
    case Projects.get_projects_by_skills(skills_filter, flop_params) do
      {:ok, res} ->
        res

      {:error, _res} ->
        {[], %{}}
    end
  end

  defp toggle_skill_id_from_list(skills_filter, id) do
    if skills_filter |> Enum.member?(id) do
      skills_filter |> List.delete(id)
    else
      [id | skills_filter]
    end
  end

  defp get_random_cards(amount),
    do: 1..22 |> Enum.to_list() |> Enum.shuffle() |> Enum.take(amount)
end
