defmodule PortfolioWeb.ExperienceLive.FormComponent do
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
        <.input field={@form[:to]} type="date" label="To" />
        <.input field={@form[:currentJob]} type="checkbox" label="Currentjob" />
        <.input field={@form[:location]} type="text" label="Location" />
        <:actions>
          <.button phx-disable-with="Saving...">Save Experience</.button>
        </:actions>
      </.simple_form>
    </div>
    """
  end

  @impl true
  def update(%{experience: experience} = assigns, socket) do
    {:ok,
     socket
     |> assign(assigns)
     |> assign_new(:form, fn ->
       to_form(Experiences.change_experience(experience))
     end)}
  end

  @impl true
  def handle_event("validate", %{"experience" => experience_params}, socket) do
    changeset = Experiences.change_experience(socket.assigns.experience, experience_params)
    {:noreply, assign(socket, form: to_form(changeset, action: :validate))}
  end

  def handle_event("save", %{"experience" => experience_params}, socket) do
    save_experience(socket, socket.assigns.action, experience_params)
  end

  defp save_experience(socket, :edit, experience_params) do
    case Experiences.update_experience(socket.assigns.experience, experience_params) do
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
end
