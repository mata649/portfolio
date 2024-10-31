defmodule PortfolioWeb.ExperienceLive.FormComponent do
  import LiveSelect
  alias Portfolio.Skills
  alias Portfolio.Skills.Skill
  use PortfolioWeb, :live_component

  alias Portfolio.Experiences

  @impl true
  def render(assigns) do
    ~H"""
    <div>
      <.header>
        <%= @title %>
        <:subtitle>Use this form to manage experience records in your database.</:subtitle>
      </.header>

      <.simple_form
        for={@form}
        id="experience-form"
        phx-target={@myself}
        phx-change="validate"
        phx-submit="save"
      >
        <.input field={@form[:company]} type="text" label="Company" />
        <.input field={@form[:position]} type="text" label="Position" />
        <.input field={@form[:description]} type="text" label="Description" />
        <.input field={@form[:from]} type="date" label="From" />
        <%= if @show_date_field do %>
          <.input field={@form[:to]} type="date" label="To" />
        <% end %>
        <.input field={@form[:current_job]} type="checkbox" label="Current" />
        <.input field={@form[:location]} type="text" label="Location" />
        <.label>
          Skills
          <.live_select
            container_extra_class="mt-2"
            dropdown_extra_class="border bg-slate-100"
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
          <.button phx-disable-with="Saving...">Save Experience</.button>
        </:actions>
      </.simple_form>
    </div>
    """
  end

  @impl true
  def update(%{experience: experience} = assigns, socket) do
    skills =
      Skills.list_skills() |> Enum.map(&value_mapper/1)

    default_skills = get_default_skills(experience)

    current_job = experience |> Map.get(:current_job, true)

    {:ok,
     socket
     |> assign(assigns)
     |> assign(:skills, skills)
     |> assign(:show_date_field, !current_job)
     |> assign_new(:form, fn ->
       to_form(Experiences.change_experience(experience, %{skills: default_skills}))
     end)}
  end

  @impl true
  def handle_event("live_select_change", %{"text" => text, "id" => live_select_id}, socket) do
    skills =
      Portfolio.Skills.search_skills_by_name(text) |> Enum.map(&value_mapper/1)

    send_update(LiveSelect.Component, id: live_select_id, options: skills)

    {:noreply, socket}
  end

  @impl true
  def handle_event("validate", %{"experience" => experience_params}, socket) do
    changeset = Experiences.change_experience(socket.assigns.experience, experience_params)
    show_date_field = !String.to_atom(experience_params["current_job"])

    {:noreply,
     assign(socket, form: to_form(changeset, action: :validate))
     |> assign(:show_date_field, show_date_field)}
  end

  def handle_event("save", %{"experience" => experience_params}, socket) do
    save_experience(socket, socket.assigns.action, experience_params)
  end

  defp save_experience(socket, :edit, experience_params) do
    case Experiences.update_experience(
           socket.assigns.experience,
           experience_params
         ) do
      {:ok, experience} ->
        notify_parent({:saved, experience})

        {:noreply,
         socket
         |> put_flash(:info, "Experience updated successfully")
         |> push_patch(to: socket.assigns.patch)}

      {:error, %Ecto.Changeset{} = changeset} ->
        {:noreply, assign(socket, form: to_form(changeset))}
    end
  end

  defp save_experience(socket, :new, experience_params) do
    case Experiences.create_experience(experience_params) do
      {:ok, experience} ->
        notify_parent({:saved, experience})

        {:noreply,
         socket
         |> put_flash(:info, "Experience created successfully")
         |> push_patch(to: socket.assigns.patch)}

      {:error, %Ecto.Changeset{} = changeset} ->
        {:noreply, assign(socket, form: to_form(changeset))}
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

  defp get_default_skills(experience) do
    case experience.skills do
      %Ecto.Association.NotLoaded{} -> []
      skills -> skills |> Enum.map(fn %Skill{id: id} -> Integer.to_string(id) end)
    end
  end
end
