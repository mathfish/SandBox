{% macro get_column_values(col_name, relation) %}

{% set relation_query %}
select distinct
    {{ col_name }}
from {{ relation }}
order by 1
{% endset %}

{% set results=run_query(relation_query) %}

{% if execute %}
{% set result_lists = results.columns[0].values() %}
{% else %}
{% set result_lists = [] %}
{% endif %}

{{ return(result_lists) }}

{% endmacro %}

{% macro get_payment_methods() %}

{{ return(get_column_values('payment_method', ref('stg_payments')))}}

{% endmacro %}

