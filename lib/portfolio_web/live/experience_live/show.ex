defmodule PortfolioWeb.ExperienceLive.Show do
  use PortfolioWeb, :live_view

  alias Portfolio.Experiences

  @impl true
  def mount(_params, _session, socket) do
    {:ok, socket}
  end

  @impl true
  def handle_params(%{"id" => id}, _, socket) do
    {:noreply,
     socket
     |> assign(:page_title, page_title(socket.assigns.live_action))
     |> assign(:experience, Experiences.get_experience!(id))}
  end

  defp page_title(:show), do: "Show Experience"
  defp page_title(:edit), do: "Edit Experience"
end
