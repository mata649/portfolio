defmodule PortfolioWeb.Home do
  alias Portfolio.Experiences
  alias Portfolio.Projects
  alias Portfolio.Skills
  use PortfolioWeb, :live_view

  def mount(_params, _session, socket) do
    skills = Skills.list_skills()
    {projects, projects_meta} = get_projects()
    experiences = Experiences.get_experiences_by_skills()

    socket =
      socket
      |> assign(:skills, skills)
      |> assign(:skills_filter, [])
      |> assign(:projects, projects)
      |> assign(:projects_meta, projects_meta)
      |> assign(:experiences, experiences)

    {:ok, socket}
  end

  def handle_event("add_skill_filter", %{"id" => id}, socket) do
    skills_filter = socket.assigns.skills_filter

    skills_filter = skills_filter |> toggle_skill_id_from_list(id)

    {projects, projects_meta} = get_projects(skills_filter)

    socket =
      socket
      |> assign(:skills_filter, skills_filter)
      |> assign(:projects, projects)
      |> assign(:projects_meta, projects_meta)

    {:noreply, socket}
  end

  def handle_event("change_page", %{"page" => page}, socket) do
    skills_filter = socket.assigns.skills_filter

    {projects, projects_meta} = get_projects(skills_filter, %Flop{page_size: 2, page: page})

    socket =
      socket
      |> assign(:projects, projects)
      |> assign(:projects_meta, projects_meta)

    {:noreply, socket}
  end

  defp get_projects(skills_filter \\ [], flop_params \\ %Flop{page_size: 2}) do
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

  def skill_item(assigns) do
    ~H"""
    <div class="p-1">
      <div
        id={"skill-#{@skill.id}"}
        class={"transition ease-out rounded-full cursor-pointer hover:scale-110  #{selected?(@selected)}"}
        phx-click="add_skill_filter"
        phx-value-id={@skill.id}
      >
        <%= @skill.name %>
      </div>
    </div>
    """
  end

  defp selected?(selected) do
    if selected, do: "font-bold text-4xl", else: "text-4xl"
  end
end
