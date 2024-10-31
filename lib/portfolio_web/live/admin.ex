defmodule PortfolioWeb.Admin do
  use PortfolioWeb, :live_view

  def mount(_params, _session, socket) do
    {:ok, socket}
  end

  attr :url, :string, required: true
  slot :inner_block

  def admin_link(assigns) do
    ~H"""
    <.link
      class="px-2 py-1 text-xl text-center transition ease-out border-2 border-black rounded-3xl hover:text-slate-800 hover:bg-slate-100"
      navigate={@url}
    >
      <%= render_slot(@inner_block) %>
    </.link>
    """
  end
end
