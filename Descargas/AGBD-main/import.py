import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

# Cargo los datos
df = pd.read_csv("Vote_Ai.csv")

# Lógica de filtrado
filtro_avanzado = df["Risk_Category"].str.startswith("H", na=False)
df_filtrado = df[filtro_avanzado]
suma_votos = df_filtrado["Total_Votes"].sum()

print("-----Reporte Automatizado-----")
print(f"Total de votos analizados {suma_votos}")

# Condicional
if (Default_limite_alto := suma_votos > 500):
    print("¡Alerta! Hay mas votos de lo esperado.")
    print("Esta todo calculado")
elif suma_votos < 100:
    print("Aviso:Este año no huvo tantos votos ")
else:
    print("Todo bien")

# Gráfico de barras usando todo el DataFrame
print("\n[Generando gráfico de barras]")
sns.set_theme(style="whitegrid")
plt.figure(figsize=(5, 6))
sns.barplot(
    data=df,
    x="Risk_Category",
    y="Total_Votes",
    estimator=sum,
    errorbar=None,
    palette="viridis"
)

plt.title("Total de votos por categoria de riesgo", fontsize=14)
plt.xticks(rotation=20)

plt.savefig("grafico_barra.png", dpi=300)
plt.close()

print("\n¡Hecho! Los graficos se gurdaran correctamente en tu carpeta")
