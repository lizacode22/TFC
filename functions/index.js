


exports.reiniciarReservasSemanal = onSchedule(
  {
    schedule: "0 0 * * 1", // Lunes a las 00:00
    timeZone: "Europe/Madrid",
  },
  async (event) => {
    try {
      const clasesSnap = await db.collection("clases").get();
      const lote = db.batch();
      const fechaActual = admin.firestore.Timestamp.now();

      for (const doc of clasesSnap.docs) {
        const datosClase = doc.data();

        // Solo guarda en historial si hay reservas
        if ((datosClase.usuarios && datosClase.usuarios.length > 0) || datosClase.inscritos > 0) {
          const historialRef = db.collection("historialClases").doc();
          await historialRef.set({
            claseId: doc.id,
            titulo: datosClase.titulo || "",
            dia: datosClase.dia || "",
            hora: datosClase.hora || "",
            capacidad: datosClase.capacidad || 0,
            usuarios: datosClase.usuarios || [],
            inscritos: datosClase.inscritos || 0,
            timestamp: fechaActual
          });
        }

        // Reiniciar los campos
        lote.update(doc.ref, {
          usuarios: [],
          inscritos: 0
        });
      }

      await lote.commit();
      console.log("Reservas reiniciadas y archivadas correctamente.");
    } catch (error) {
      console.error("Error al reiniciar y archivar reservas:", error);
    }
  }
);