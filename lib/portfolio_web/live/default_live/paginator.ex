defmodule PortfolioWeb.DefaultLive.Paginator do
  use Phoenix.LiveComponent

  def render(assigns) do
    ~H"""
    <div class="flex justify-center w-full gap-3">
      <%= if @meta.total_pages != 0 do %>
        <%= if @meta.current_page != 1 do %>
          <span
            class="transition ease-out delay-75 cursor-pointer hover:scale-110"
            phx-click="change_page"
            phx-value-page={@meta.current_page - 1}
          >
            ◄
          </span>
        <% end %>
        <%= for i <- Enum.to_list(1..@meta.total_pages) do %>
          <span
            class={"transition ease-out delay-75  cursor-pointer hover:scale-110 #{if i == @meta.current_page, do: "font-bold text-xl", else: "text-lg"}"}
            phx-click="change_page"
            phx-value-page={i}
          >
            <%= i %>
          </span>
        <% end %>
        <%= if @meta.current_page != @meta.total_pages do %>
          <span
            class="transition ease-out delay-75 cursor-pointer hover:scale-110"
            phx-click="change_page"
            phx-value-page={@meta.current_page + 1}
          >
            ►
          </span>
        <% end %>
      <% end %>
    </div>
    """
  end
end
