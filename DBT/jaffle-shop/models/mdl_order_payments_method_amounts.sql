{%- set payment_methods = get_payment_methods() -%}

select
    order_id,
    {%- for payment_method in payment_methods -%}
    coalesce(sum(case when payment_method = '{{payment_method}}' then amount end), 0) {{payment_method}}_amount,
    {% endfor %}
    sum(amount) total_amount
from {{ ref("stg_payments") }}
group by 1