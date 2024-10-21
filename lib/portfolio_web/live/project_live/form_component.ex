defmodule PortfolioWeb.ProjectLive.FormComponent do
  alias Portfolio.Skills.Skill
  alias Portfolio.Skills
  import LiveSelect
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
        <.label>
          Skills
          <.live_select
            container_extra_class="mt-2"
            dropdown_extra_class="border bg-yellow-100"
            field={@form[:skills]}
            phx-target={@myself}
            mode={:tags}
            options={@skills}
          />
        </.label>
        <div phx-feedback-for={@form[:skills].name}>
          <.error :for={msg <- Enum.map(@form[:skills].errors, &translate_error(&1))}>
            <%= msg %>
          </.error>
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
    skills =
      Skills.list_skills() |> Enum.map(&value_mapper/1)

    default_skills = get_default_skills(project)

    {
      :ok,
      socket
      |> assign(assigns)
      |> assign(:skills, skills)
      |> assign_new(:form, fn ->
        to_form(Projects.change_project(project, %{skills: default_skills}))
      end)
    }
  end

  @impl true
  def handle_event("live_select_change", %{"text" => text, "id" => live_select_id}, socket) do
    skills =
      Portfolio.Skills.search_skills_by_name(text) |> Enum.map(&value_mapper/1)

    send_update(LiveSelect.Component, id: live_select_id, options: skills)

    {:noreply, socket}
  end

  @impl true
  def handle_event("validate", %{"project" => project_params}, socket) do
    changeset =
      Projects.change_project(
        socket.assigns.project,
        project_params
      )

    {:noreply, assign(socket, form: to_form(changeset, action: :validate))}
  end

  def handle_event("save", %{"project" => project_params}, socket) do
    save_project(socket, socket.assigns.action, project_params)
  end

  defp save_project(socket, :edit, project_params) do
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

  defp value_mapper(%Skill{
         id: id,
         name: name
       }) do
    %{
      label: name,
      value: Integer.to_string(id),
      tag_label: name
    }
  end

  defp get_default_skills(project) do
    case project.skills do
      %Ecto.Association.NotLoaded{} -> []
      skills -> skills |> Enum.map(fn %Skill{id: id} -> Integer.to_string(id) end)
    end
  end
end
