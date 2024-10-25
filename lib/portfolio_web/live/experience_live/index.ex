defmodule PortfolioWeb.ExperienceLive.Index do
  use PortfolioWeb, :live_view

  alias Portfolio.Experiences
  alias Portfolio.Experiences.Experience

  @impl true
  def mount(_params, _session, socket) do
    {:ok, stream(socket, :experiences, Experiences.list_experiences([:skills]))}
  end

  @impl true
  def handle_params(params, _url, socket) do
    {:noreply, apply_action(socket, socket.assigns.live_action, params)}
  end

  defp apply_action(socket, :edit, %{"id" => id}) do
    socket
    |> assign(:page_title, "Edit Experience")
    |> assign(:experience, Experiences.get_experience!(id, [:skills]))
  end

  defp apply_action(socket, :new, _params) do
    socket
    |> assign(:page_title, "New Experience")
    |> assign(:experience, %Experience{})
  end

  defp apply_action(socket, :index, _params) do
    socket
    |> assign(:page_title, "Listing Experiences")
    |> assign(:experience, nil)
  end

  @impl true
  def handle_info({PortfolioWeb.ExperienceLive.FormComponent, {:saved, experience}}, socket) do
    {:noreply, stream_insert(socket, :experiences, experience)}
  end

  @impl true
  def handle_event("delete", %{"id" => id}, socket) do
    experience = Experiences.get_experience!(id)
    {:ok, _} = Experiences.delete_experience(experience)

    {:noreply, stream_delete(socket, :experiences, experience)}
  end
end
