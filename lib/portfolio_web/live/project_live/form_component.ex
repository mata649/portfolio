defmodule PortfolioWeb.ProjectLive.FormComponent do
  alias Portfolio.Skills.Skill
  alias Portfolio.Projects.Project
  alias Portfolio.Skills
  use PortfolioWeb, :live_component

  alias Portfolio.Projects

  @impl true
  def render(assigns) do
    ~H"""
    <div>
      <.header>
        <%= @title %>
      </.header>

      <.simple_form
        for={@form}
        id="project-form"
        phx-target={@myself}
        phx-change="validate"
        phx-submit="save"
      >
        <.input field={@form[:name]} type="text" label="Name" />
        <.input field={@form[:description]} type="textarea" label="Description" />
        <.input field={@form[:githubURL]} type="text" label="Github URL" />
        <div class="flex flex-col items-center gap-3">
          <label>Skills</label>
          <.error :for={msg <- Enum.map(@form[:skills].errors, &translate_error/1)}>
            <%= msg %>
          </.error>
          <div class="flex justify-center gap-3">
            <%= for skill <- @skills do %>
              <span
                id={"skill-#{skill.id}"}
                class="p-1 transition ease-in-out border-2 rounded-full cursor-pointer hover:scale-110"
                style={"border-color: #{skill.color}; #{
                  if length(Enum.filter(@selected_skills, fn %Skill{id: id} ->  id == skill.id end)) > 0 do
                  "background-color: #{skill.color}; color: white;" end}"}
                phx-target={@myself}
                phx-click="toggle_selected_skill"
                phx-value-id={skill.id}
              >
                <%= skill.name %>
              </span>
            <% end %>
          </div>
        </div>
        <:actions>
          <.button phx-disable-with="Saving...">Save Project</.button>
        </:actions>
      </.simple_form>
    </div>
    """
  end

  @impl true
  def update(%{project: project} = assigns, socket) do
    skills = Skills.list_skills()

    selected_skills =
      case project.skills do
        %Ecto.Association.NotLoaded{} -> []
        skills -> skills
      end

    {:ok,
     socket
     |> assign(assigns)
     |> assign(:skills, skills)
     |> assign(:selected_skills, selected_skills)
     |> assign_new(:form, fn ->
       to_form(Projects.change_project(project))
     end)}
  end

  @impl true
  def handle_event("validate", %{"project" => project_params}, socket) do
    skills = socket.assigns.selected_skills

    changeset =
      Projects.change_project(socket.assigns.project, project_params |> Map.put("skills", skills))

    {:noreply, assign(socket, form: to_form(changeset, action: :validate))}
  end

  def handle_event("save", %{"project" => project_params}, socket) do
    save_project(socket, socket.assigns.action, project_params)
  end

  def handle_event("toggle_selected_skill", %{"id" => id} = _params, socket) do
    selected_skills = socket.assigns.selected_skills

    skill =
      socket.assigns.skills
      |> Enum.find(fn %Skill{id: skill_id} -> skill_id == String.to_integer(id) end)

    selected_skills =
      cond do
        Enum.member?(selected_skills, skill) ->
          Enum.filter(selected_skills, fn %Skill{id: selected_id} -> selected_id != skill.id end)

        true ->
          [skill | selected_skills]
      end

    {:noreply, assign(socket, :selected_skills, selected_skills)}
  end

  defp save_project(socket, :edit, project_params) do
    skills = socket.assigns.selected_skills
    project_params = project_params |> Map.put("skills", skills)

    case Projects.update_project(socket.assigns.project, project_params) do
      {:ok, project} ->
        notify_parent({:saved, project})

        {:noreply,
         socket
         |> put_flash(:info, "Project updated successfully")
         |> push_patch(to: socket.assigns.patch)}

      {:error, %Ecto.Changeset{} = changeset} ->
        {:noreply, assign(socket, form: to_form(changeset))}
    end
  end

  defp save_project(socket, :new, project_params) do
    skills = socket.assigns.selected_skills
    project_params = project_params |> Map.put("skills", skills)

    case Projects.create_project(project_params) do
      {:ok, project} ->
        notify_parent({:saved, project})

        {:noreply,
         socket
         |> put_flash(:info, "Project created successfully")
         |> push_patch(to: socket.assigns.patch)}

      {:error, %Ecto.Changeset{} = changeset} ->
        {:noreply,
         assign(socket,
           form: to_form(changeset)
         )}
    end
  end

  defp notify_parent(msg), do: send(self(), {__MODULE__, msg})
end
